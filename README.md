📡 Sistema FTP Cliente-Servidor (Java + Python)

Projeto completo de FTP (File Transfer Protocol) com:

🖥 Servidor FTP em Python (com interface)

💻 Cliente FTP em Java (Swing)

🔗 Comunicação via protocolo FTP real

📌 Descrição do Projeto

Este projeto implementa um sistema de transferência de arquivos baseado no protocolo FTP, utilizando o modelo cliente-servidor.

O sistema é composto por:

Um servidor em Python, responsável por gerenciar conexões, usuários e arquivos

Um cliente em Java, que permite interação com o servidor através de interface gráfica

A comunicação ocorre via TCP, permitindo operações como:

Upload de arquivos

Download de arquivos

Listagem de diretórios

Exclusão de arquivos

🚀 Funcionalidades 🖥 Servidor (Python)

Interface gráfica (Tkinter)

Criação de usuário e senha

Seleção de pasta FTP

Logs em tempo real

Upload e download de arquivos

Monitoramento de conexões

💻 Cliente (Java)

Interface gráfica (Swing)

Conexão com servidor FTP

Listagem de arquivos

Download de arquivos

Upload de arquivos

Exclusão de arquivos

Logs de operações

🛠 Tecnologias utilizadas

Backend (Servidor)

Python 3

pyftpdlib

Cliente

Java

Swing

Apache Commons Net

IDE

NetBeans

Cursor

📦 Instalação

🔹 Servidor (Python)

Instalar dependência:
```bash
pip install pyftpdlib
```

Executar o servidor:
```bash
python serverFTP.py
```

▶️ Como Executar (Cliente e Servidor) 🔹 1. Iniciar o Servidor

Execute o arquivo Python

Defina:

Usuário

Senha

Pasta compartilhada

Clique em "Iniciar Servidor"

🔹 2. Executar o Cliente

Compile e execute ClienteFTP.java

Ou execute pela IDE (NetBeans)

Preencha:

IP do servidor (ex: 127.0.0.1)

Porta: 2121

Usuário e senha

Clique em Conectar

🔐 Explicação da Criptografia

O sistema utiliza o protocolo FTP tradicional, que não possui criptografia nativa.

Isso significa que:

Usuário e senha são enviados em texto simples

🏗 Arquitetura da Solução

O sistema segue o modelo cliente-servidor:

🔹 Servidor

Implementado em Python com pyftpdlib

Responsável por:

Autenticação de usuários

Gerenciamento de arquivos

Controle de conexões

Utiliza um Handler personalizado para registrar eventos:

Conexão

Login

Upload

Download

🔹 Cliente

Implementado em Java com Swing

Responsável por:

Interface com o usuário

Envio de comandos FTP

Manipulação de arquivos

🔹 Comunicação

Protocolo: FTP

Transporte: TCP

Porta utilizada: 2121

Modo: Passivo (PASV)

🔹 Fluxo de Funcionamento

Cliente conecta ao servidor (controle)

Usuário realiza login

Cliente envia comandos:

Listar arquivos

Enviar

Baixar

Deletar

Servidor processa e responde

Transferência ocorre em uma conexão de dados separada

💡 Observações Importantes

Uso de modo passivo, evitando problemas com firewall

Transferência em modo binário, evitando corrupção de arquivos

Verificação de conexão com o servidor no cliente

Uso de threads no servidor, evitando travamento da interface

Sistema com interface gráfica completa

🎯 Conclusão

Este projeto demonstra na prática o funcionamento do protocolo FTP dentro do modelo cliente-servidor, abordando conceitos como:

Comunicação em rede

Transferência de arquivos

Autenticação

Controle de conexão

Interface gráfica