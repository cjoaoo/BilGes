# BilGes
Trabalho realizado no contexto da disciplina Construção de Sistemas de Software, originalmente desenvolvido no GitLab da FCUL.

### Autores:
Catarina Fernandes
Cláudia Ferreira
João Gil

### BilGes:
O BilGes é um sistema de apoio à venda de bilhetes.
As instalações geridas pelo sistema permitem a realização de eventos, e em particular, atividades desportivas. O sistema irá permitir que os gestores realizem a criação de eventos de diferentes tipos, que podem ocorrer numa única data ou em várias datas consecutivas. Posteriormente, o sistema permite definir a instalação em que os eventos vão decorrer e a data a partir da qual os bilhetes para o evento devem ser colocados à venda. O sistema suporta a venda de bilhetes individuais, para um dia específico e um evento, e bilhetes-passe, para todas as datas de um evento. O sistema suporta a escolha de lugares na venda de bilhetes individuais para eventos com lugares sentados. Na venda de bilhetes-passe para eventos com lugares sentados, a escolha dos lugares é feita pelo sistema.

### Instruções:
No Eclipse, para iniciar a base de dados Derby:

Run as application src/main/java/dbutils/RunSQLScript.java
Run as application src/main/java/dbutils/ResetAndCreateDatabase.java

Para testar o cliente:
Run as application src/main/java/SimpleClient.java

Para correr na base de dados Derby:
Na pasta src/main/resources/META-INF
- fazer copy paste dos conteúdos de persistence-derby.xml para a secção source do persistence. xml
- fazer copy paste dos conteúdos de load-script-derbyl.sql para load-script.sql

Para correr na base de dados MySql: (Só funciona na VPN da FCUL)
Na pasta src/main/resources/META-INF
- fazer copy paste dos conteúdos de persistence-mysql.xml para a secção source do persistence. xml
- fazer copy paste dos conteúdos de load-script-mysql.sql para load-script.sql

### Cliente teste: 

Para escrever o SimpleClient e para a inicialização da base de dados considerem os dados indicados abaixo. Porque há várias operações que são sensíveis à data corrente e queremos que a execução do SimpleClient seja determinística, definam uma classe com um método que é suposto dar a data corrente, mas que tem uma implementação mock que devolve sempre 1/05/2021. Usem esse método para obter a data corrente sempre que ela for precisa (no código do negócio e no SimpleClient).

#### Tipos de Eventos:
Existem 3 tipos de eventos:
- TeteATete – os lugares são sentados, com assistência máxima de 6 pessoas
- BandoSentado – os lugares são sentados, com assistência máxima de 1 000 pessoas
- MultidaoEmPe – os lugares são em pé, com assistência máxima de 500 000 pessoas

#### Instalações e Lugares
Existem 3 instalações: 
Micro Pavilhao, Mini Estadio e Pequeno Relvado. 
O Micro Pavilhao tem os seguintes lugares individuais: A-1,A-2,B-1,B-2,B-3. 
O Mini Estadio tem os seguintes lugares individuais: A-1 a A-5 e B-1 a B-10. 
O Pequeno Relvado não tem lugares individuais e tem capacidade para 10 pessoas.

#### Promotras:
Existem 2 empresas produtoras:
| num:       | nome:      | tipos de eventos:
| 1          | una        | TeteATete, BandoSentado
| 2          | dua        | BandoSentado, MultidaoEmPe






