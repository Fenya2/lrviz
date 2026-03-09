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

# Источники

- https://kadm.kmath.ru/files/loi7sem5.pdf
- https://kadm.kmath.ru/files/loi7sem9_1.pdf
- [компиляторы](https://github.com/lehaSVV2009/Compiler/blob/master/books/%D0%90%D1%85%D0%BE,%20%D0%A1%D0%B5%D1%82%D0%B8,%20%D0%A3%D0%BB%D1%8C%D0%BC%D0%B0%D0%BD.%20%D0%9A%D0%BE%D0%BC%D0%BF%D0%B8%D0%BB%D1%8F%D1%82%D0%BE%D1%80%D1%8B.%20%D0%9F%D1%80%D0%B8%D0%BD%D1%86%D0%B8%D0%BF%D1%8B,%20%D1%82%D0%B5%D1%85%D0%BD%D0%BE%D0%BB%D0%BE%D0%B3%D0%B8%D0%B8,%20%D0%B8%D0%BD%D1%81%D1%82%D1%80%D1%83%D0%BC%D0%B5%D0%BD%D1%82%D1%8B.2ed.2008.pdf)
- https://rahul.gopinath.org/post/2024/07/01/lr-parsing/#lalr1-automata
- https://sourceforge.net/projects/jsmachines/
- https://www.cs.cornell.edu/courses/cs4120/2022sp/notes.html?id=lr
- https://www.dickgrune.com/Books/PTAPG_1st_Edition/BookBody.pdf
- https://dpvipracollege.ac.in/wp-content/uploads/2023/01/Alfred-V.-Aho-Monica-S.-Lam-Ravi-Sethi-Jeffrey-D.-Ullman-Compilers-Principles-Techniques-and-Tools-Pearson_Addison-Wesley-2007.pdf
- https://simondlevy.academic.wlu.edu/files/courses/cs332w2004/lectures/23_FEB_2004.pdf