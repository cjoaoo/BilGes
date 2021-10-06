# BilGes
Trabalho realizado no contexto da disciplina Construção de Sistemas de Software da Licenciatura em Engenharia Informatica da FCUL. Originalmente desenvolvido no GitLab da FCUL.

### Autores:
- Catarina Fernandes
- Cláudia Ferreira
- João Gil

### BilGes:
O BilGes é um sistema de apoio à venda de bilhetes.
As instalações geridas pelo sistema permitem a realização de eventos, e em particular, atividades desportivas. O sistema irá permitir que os gestores realizem a criação de eventos de diferentes tipos, que podem ocorrer numa única data ou em várias datas consecutivas. Posteriormente, o sistema permite definir a instalação em que os eventos vão decorrer e a data a partir da qual os bilhetes para o evento devem ser colocados à venda. O sistema suporta a venda de bilhetes individuais, para um dia específico e um evento, e bilhetes-passe, para todas as datas de um evento. O sistema suporta a escolha de lugares na venda de bilhetes individuais para eventos com lugares sentados. Na venda de bilhetes-passe para eventos com lugares sentados, a escolha dos lugares é feita pelo sistema.

### Instruções:
No Eclipse, para iniciar a base de dados Derby:
- Run as application `src/main/java/dbutils/ResetAndCreateDatabase.java`

Para testar o cliente (19 casos teste):
- Run as application `src/main/java/client/SimpleClient.java`

Nota: Os casos 4, 5, 7, 9, 13, 15 e 19 devem ser apresentadas mensagem de erro com justificação na consola.

### Para mudar de base de dados:
Para correr na base de dados Derby:
- fazer copy paste dos conteúdos de `src/main/resources/META-INF/persistence-derby.xml` para a secção source do `persistence.xml`
- fazer copy paste dos conteúdos de `src/main/resources/META-INF/load-script-derbyl.sql` para `load-script.sql`

Para correr na base de dados MySql: (Só funciona na VPN da FCUL)
- fazer copy paste dos conteúdos de `src/main/resources/META-INF/persistence-mysql.xml` para a secção source do `persistence.xml`
- fazer copy paste dos conteúdos de `src/main/resources/META-INF/load-script-mysql.sql` para `load-script.sql`

### Cliente teste: 
Tem 19 casos teste. Porque há várias operações que são sensíveis à data corrente e queremos que a execução do SimpleClient seja determinística, é definida uma classe com um método que é suposto dar a data corrente, mas que tem uma implementação mock que devolve sempre 1/05/2021. Este método é usado para obter a data corrente sempre que ela for precisa (no código do negócio e no SimpleClient).

#### Tipos de Eventos:
Existem 3 tipos de eventos:
- TeteATete – os lugares são sentados, com assistência máxima de 6 pessoas
- BandoSentado – os lugares são sentados, com assistência máxima de 1 000 pessoas
- MultidaoEmPe – os lugares são em pé, com assistência máxima de 500 000 pessoas.

#### Instalações e Lugares
Existem 3 instalações: 
- O Micro Pavilhao, com os seguintes lugares individuais: A-1,A-2,B-1,B-2,B-3. 
- O Mini Estadio, com os seguintes lugares individuais: A-1 a A-5 e B-1 a B-10. 
- O Pequeno Relvado, que não tem lugares individuais e tem capacidade para 10 pessoas.

#### Promotras:
Existem 2 empresas produtoras:
- una (1), autorizada para os tipos de evento TeteATete e BandoSentado
- dua (2), autorizada para os tipos de evento BandoSentado e MultidaoEmPe.

