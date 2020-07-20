###Домашняя работа №4. GC

<p>Домашняя работа для изучения работы GC, осваивании инструментов мониторинга работы JVM.</p>

В качестве GC использовались:
+ G1
+ Serial Collector
+ Parallel Collector

<h3>Однопоточный режим</h3>

В качестве подопытной программы использовался класс `ListOOM`, основанный на постоянно увеличивающимся списке.
Используемые параметры:
```shell script
java -Xms200m -Xmx200m -verbose:gc -Xlog:gc*:file=./logs/gc_pid_%p.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump
```
<p>Результаты:</p>

<table>
    <thead>
        <tr>
            <th rowspan="2">GC</th>
            <th colspan="5">Первый запуск</th>
            <th colspan="5">Второй запуск</th>
            <th colspan="5">Третий запуск</th>
            <th rowspan="2">Комментарий</th>
        </tr>
        <tr>
            <th>Количество элементов</th>
            <th>Время работы</th>
            <th>Время первого срабатывания полной сборки</th>
            <th>Длительность первого срабатывания полной сборки</th>
            <th>Порог срабатывания</th>
            <th>Количество элементов</th>
            <th>Время работы</th>
            <th>Время первого срабатывания полной сборки</th>
            <th>Длительность первого срабатывания полной сборки</th>
            <th>Порог срабатывания
            <th>Количество элементов</th>
            <th>Время работы</th>
            <th>Время первого срабатывания полной сборки</th>
            <th>Длительность первого срабатывания полной сборки</th>
            <th>Порог срабатывания
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>G1 (Java heap space)</td>
            <td>~2451112</td>
            <td>261.948s</td>
            <td>194.992s</td>
            <td>149.026ms</td>
            <td>199M->177M(200M)</td>
            <td>~2451864</td>
            <td>258.692s</td>
            <td>188.462s</td>
            <td>123.947ms</td>
            <td>200M->176M(200M)</td>
            <td>~2451800</td>
            <td>312.890s</td>
            <td>244.429s</td>
            <td>128.897ms</td>
            <td>200M->179M(200M)</td>
            <td>Как ни странно, но финальное количество элементов в списке не вывелось.</td>
        </tr>
        <tr>
            <td>Serial (Java heap space)</td>
            <td>2384338</td>
            <td>281.276s</td>
            <td>51.320s</td>
            <td>142.414ms</td>
            <td>187M->109M(193M)</td>
            <td>2384330</td>
            <td>273.419s</td>
            <td>56.663s</td>
            <td>167.364ms</td>
            <td>187M->109M(193M)</td>
            <td>2384370</td>
            <td>325.922s</td>
            <td>68.079s</td>
            <td>157.236ms</td>
            <td>190M->112M(193M)</td>
            <td>Отработало как и ожидалось</td>
        </tr>
        <tr>
            <td>Parallel (GC overhead limit exceeded)</td>
            <td>2094891</td>
            <td>242.314s</td>
            <td>68.626s</td>
            <td>256.154ms</td>
            <td>188M->107M(184M)</td>
            <td>2113442</td>
            <td>206.649s</td>
            <td>63.631s</td>
            <td>278.148ms</td>
            <td>188M->106M(183M)</td>
            <td>2191163</td>
            <td>226.154s</td>
            <td>62.986s</td>
            <td>232.501ms</td>
            <td>183M->105M(186M)</td>
            <td>Процесс завершился тк нехватило памяти для очередного запуска GC</td>
        </tr>
    </tbody>
</table>

Вывод:
+ Полная сборка у G1 происходит на более поздних этапах, когда старое поколение заполняется. Время сборки меньше, чем у остальных двух. При этом использование хипа программой больше.  
+ При использовании Parallel необходимо учитывать выделение памяти для самого сборщика мусора. Время отрабатывания больше, чем у всех остальных. Но первое срабатывание происходит позже, чем у Serial Collector. Программе выделено меньше всего памяти в хипе.
+ Serial в данном случае оказался полезнее, чем Parallel Collector, но хуже, чем G1. Но он первый произвел полную сборку. В остальном он на втором месте.

В проведенном анализе G1 оказался лучшим, по этому, при возможности, лучше всего использовать именно его.  

<h3>Многопоточный режим</h3>

В качестве подопытной программы использовался класс `ListOOM`, основанный на постоянно увеличивающимся списке.
Используемые параметры:
```shell script
java -Xms200m -Xmx200m -verbose:gc -Xlog:gc*:file=./logs/gc_pid_%p.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump
```