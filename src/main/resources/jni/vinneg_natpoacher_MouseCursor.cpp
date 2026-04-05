#include <jni.h>
#include <windows.h>
#include <wingdi.h>
#include <wincrypt.h>
#include <sstream>
#include <iomanip>
#include "vinneg_natpoacher_MouseCursor.h"

// Простейшая реализация MD5 (упрощённая для примера)
// В реальном проекте лучше использовать готовую библиотеку

// MD5 хэширование через CryptAPI (Windows)
std::string MD5Hash(BYTE* data, size_t len) {
    HCRYPTPROV hProv;
    HCRYPTHASH hHash;
    DWORD dwHashLen = 16;
    BYTE hash[16];
    std::stringstream ss;

    if (!CryptAcquireContext(&hProv, NULL, NULL, PROV_RSA_FULL, CRYPT_VERIFYCONTEXT)) return "";
    if (!CryptCreateHash(hProv, CALG_MD5, 0, 0, &hHash)) {
        CryptReleaseContext(hProv, 0);
        return "";
    }
    if (!CryptHashData(hHash, data, len, 0)) {
        CryptDestroyHash(hHash);
        CryptReleaseContext(hProv, 0);
        return "";
    }
    if (!CryptGetHashParam(hHash, HP_ALGID, (BYTE*)&dwHashLen, &dwHashLen, 0)) {
        CryptDestroyHash(hHash);
        CryptReleaseContext(hProv, 0);
        return "";
    }

    CryptGetHashParam(hHash, HP_HASHVAL, hash, &dwHashLen, 0);

    CryptDestroyHash(hHash);
    CryptReleaseContext(hProv, 0);

    for (int i = 0; i < 16; i++) {
        ss << std::setfill('0') << std::setw(2) << std::hex << (int)hash[i];
    }
    return ss.str();
}

JNIEXPORT jstring JNICALL Java_vinneg_natpoacher_MouseCursor_getCursorHash(JNIEnv* env, jobject) {
    CURSORINFO ci = {0};
    ci.cbSize = sizeof(CURSORINFO);

    if (!GetCursorInfo(&ci)) {
        return env->NewStringUTF("error: GetCursorInfo failed");
    }

    ICONINFO ii = {0};
    if (!GetIconInfo(ci.hCursor, &ii)) {
        return env->NewStringUTF("error: GetIconInfo failed");
    }

    // Работаем только с изображением (без маски)
    // Получаем DIB биты
    HDC hdcScreen = GetDC(NULL);
    HDC hdc = CreateCompatibleDC(hdcScreen);

    // Получаем размеры
    BITMAP bmp;
    GetObject(ii.hbmColor, sizeof(BITMAP), &bmp);

    // Создаём DIB
    BITMAPINFOHEADER bi = {0};
    bi.biSize = sizeof(BITMAPINFOHEADER);
    bi.biWidth = bmp.bmWidth;
    bi.biHeight = -bmp.bmHeight;  // Вертикальное отражение
    bi.biPlanes = 1;
    bi.biBitCount = 32;
    bi.biCompression = BI_RGB;

    BYTE* pixels = nullptr;
    HBITMAP hbmp = CreateDIBSection(hdc, (BITMAPINFO*)&bi, DIB_RGB_COLORS, (void**)&pixels, NULL, 0);

    HGDIOBJ old = SelectObject(hdc, hbmp);
    DrawIconEx(hdc, 0, 0, ci.hCursor, 0, 0, 0, NULL, DI_NORMAL | DI_IMAGE);
    SelectObject(hdc, old);

    // Хэшируем только цветные пиксели (игнорируя альфа-канал, если нужно)
    std::string hash = MD5Hash(pixels, bmp.bmWidth * bmp.bmHeight * 4);

    // Очистка
    DeleteObject(hbmp);
    DeleteDC(hdc);
    ReleaseDC(NULL, hdcScreen);
    if (ii.hbmColor) DeleteObject(ii.hbmColor);
    if (ii.hbmMask) DeleteObject(ii.hbmMask);

    return env->NewStringUTF(hash.c_str());
}
