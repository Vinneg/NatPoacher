#include <jni.h>
#include <windows.h>
#include <wingdi.h>
#include <wincrypt.h>
#include <sstream>
#include <iomanip>
#include <vector>
#include "vinneg_natpoacher_MouseCursor.h"

JNIEXPORT jbyteArray JNICALL Java_vinneg_natpoacher_MouseCursor_getCursor(JNIEnv* env, jobject) {
    CURSORINFO ci = {sizeof(CURSORINFO)};
    if (!GetCursorInfo(&ci)) {
        return nullptr;
    }

    ICONINFO ii = {0};
    if (!GetIconInfo(ci.hCursor, &ii)) {
        return nullptr;
    }

    // Проверяем, есть ли цветное изображение
    if (ii.hbmColor == NULL) {
        DeleteObject(ii.hbmMask);
        return nullptr; // Монохромный курсор
    }

    // Создаём совместимый контекст устройства
    HDC hdcScreen = GetDC(NULL);
    HDC hdcMem = CreateCompatibleDC(hdcScreen);

    // Выбираем битовое изображение в контекст
    HBITMAP hbmOld = (HBITMAP)SelectObject(hdcMem, ii.hbmColor);

    // Получаем информацию о битовом образе
    BITMAP bm;
    GetObject(ii.hbmColor, sizeof(BITMAP), &bm);

    // Заполняем структуру BITMAPINFO
    BITMAPINFO bmi = {};
    bmi.bmiHeader.biSize = sizeof(BITMAPINFOHEADER);
    bmi.bmiHeader.biWidth = bm.bmWidth;
    bmi.bmiHeader.biHeight = -bm.bmHeight; // Отрицательная высота для прямого порядка строк
    bmi.bmiHeader.biPlanes = 1;
    bmi.bmiHeader.biBitCount = 32; // 32 бита на пиксель (RGBA)
    bmi.bmiHeader.biCompression = BI_RGB;

    // Рассчитываем размер буфера
    int rowSize = ((bm.bmWidth * 32 + 31) / 32) * 4; // Выравнивание по 4 байтам
    int bufferSize = rowSize * bm.bmHeight;

    std::vector<BYTE> pixelData(bufferSize);

    // Извлекаем пиксельные данные
    if (!GetDIBits(hdcMem, ii.hbmColor, 0, bm.bmHeight, pixelData.data(), &bmi, DIB_RGB_COLORS)) {
        pixelData.clear();
    }

    // Очищаем ресурсы
    SelectObject(hdcMem, hbmOld);
    DeleteDC(hdcMem);
    ReleaseDC(NULL, hdcScreen);
    if (ii.hbmColor) DeleteObject(ii.hbmColor);
    if (ii.hbmMask) DeleteObject(ii.hbmMask);

    jbyteArray result = env->NewByteArray(pixelData.size());
    env->SetByteArrayRegion(result, 0, pixelData.size(), reinterpret_cast<jbyte*>(pixelData.data()));

    return result;
}
