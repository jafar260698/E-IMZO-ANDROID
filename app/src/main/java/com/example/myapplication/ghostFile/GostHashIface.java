package com.example.myapplication.ghostFile;

public interface GostHashIface {
    /**
     * Вернуть признак типа реализации
     * @return true, если реализовано через JNI-библиотеку.
     */
    boolean isNative();

    /**
     * Первоначальная инициализация
     */
    void init();

    /**
     * Итоговая очистка
     */
    void done();

    /**
     * Подготовка к расчету хеш-суммы
     */
    void startHash();

    /**
     * Добавить в хеш очередной блок данных
     * @param block
     * @param pos
     * @param length
     */
    void hashBlock(byte[] block, int pos, int length);

    /**
     * Получить результат - хеш-сумму
     * @return Подсчитанное значение хэша
     */
    byte[] finishHash();

    /**
     * Подсчитать контрольную сумму файла
     * При ошибке ввода-вывода (включая отсутствие файла) вернуть null.
     * @param file
     * @return Подсчитанное значение хэша
     */
    byte[] calcHash(java.io.File file);

}
