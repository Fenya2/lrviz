Веб приложение, предоставляющее API и веб-интерфейс для изучения алгоритмов построения различных LR-автоматов.

# Сборка

```bash
git clone git@github.com:Fenya2/lrviz-back.git
cd ./lrviz-back
mvn install
```

# Запуск

Для экспортирования картинок на сервере с развернутым приложением требуется установленное ПО для визуализации
графов [graphviz](https://graphviz.org/)

```bash
sudo apt install graphviz
```

```bash
java -jar ./target/*.jar
```

# Веб-интерфейс

```
http://localhost:8080
```

# Документация к API

```
http://localhost:8080/api/docs/guide.html
```