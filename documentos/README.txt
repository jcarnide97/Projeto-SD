Manual de Uso eVoting: Voto Eletrónico na UC
1. Abra 2 ficheiros rmiserver.jar - o primeiro aberto servirá como servidor RMI
 primário e o seguinte como secundário
2. Abra o ficheiro console.jar - este será a consola de administração onde poderá 
fazer diversas funções (por exemplo: criar um utilizador, criar uma eleição, criar 
listas condidatas a uma eleição, consultar resultados de eleições passadas, etc.)
3. Abra o ficheiro server.jar - este ficheiro será o servidor Multicast que servirá 
como mesa de voto onde os diferentes utilizadores, já previamente criados na consola 
de administração, poderão fazer a sua autenticação para poderem votar (atráves de nome 
e número)
4. Abra o ficheiro terminal.jar - este ficheiro será o ficheiro onde serão feitos os 
votos de uma eleição. Inicialmente o utilizador faz o login, através de nome e password 
(código de acesso gerado na criação do utilizador), e terá ao seu dispor todas as listas
candidatas à eleição (assim como a opção de votar em branco ou de o voto ser considerado
como nulo)