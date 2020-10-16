import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        GameProgress gp1 = new GameProgress(100, 2, 44, 10.0);//создаём "игровые" объекты
        GameProgress gp2 = new GameProgress(90, 1, 23, 9.1);
        GameProgress gp3 = new GameProgress(1, 1, 5, 3.4);

        String zipPath = "D:\\MyGame\\gamedata.zip";//путь где будет размещён архив + имя
        String[] filePath = {"D:\\MyGame\\savegame0.txt", "D:\\MyGame\\savegame1.txt", "D:\\MyGame\\savegame2.txt"};//файлы сохранений

        saveGame(filePath[0], gp1);//сохраняем объекты
        saveGame(filePath[1], gp2);
        saveGame(filePath[2], gp3);

        zipFiles(zipPath, filePath);//архивируем
        deleteUnpackedFiles(filePath);//удаляем ненужное
    }

    public static void saveGame(String path, GameProgress gameProgress) {
        // откроем выходной поток для записи в файл
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {// запишем экземпляр класса в файл
            oos.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String zipPath, String[] filePath) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (int i = 0; i < filePath.length; i++) {
                FileInputStream fis = new FileInputStream(filePath[i]);
                ZipEntry entry = new ZipEntry("packed_save" + (i + 1) + ".txt");
                zout.putNextEntry(entry);// считываем содержимое файла в массив byte
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);// добавляем содержимое к архиву
                zout.write(buffer);// закрываем текущую запись для новой записи
                fis.close();
            }
            zout.closeEntry();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void deleteUnpackedFiles(String[] filePath) {
        for (int i = 0; i < filePath.length; i++) {
            File file = new File(filePath[i]);
            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
            } else {
                System.out.println("Delete operation is failed.");
            }
        }
    }
}
