import os
import threading
import tkinter as tk
from tkinter import filedialog, messagebox

from pyftpdlib.authorizers import DummyAuthorizer
from pyftpdlib.handlers import FTPHandler
from pyftpdlib.servers import FTPServer

server = None


# HANDLER PERSONALIZADO
class MeuFTPHandler(FTPHandler):

    def on_connect(self):
        log(f"Cliente conectado: {self.remote_ip}")

    def on_disconnect(self):
        log(f"Cliente desconectado: {self.remote_ip}")

    def on_login(self, username):
        log(f"Login realizado: {username}")

    def on_logout(self, username):
        log(f"Logout: {username}")

    def on_file_sent(self, file):
        log(f"Download realizado: {os.path.basename(file)}")

    def on_file_received(self, file):
        log(f"Upload realizado: {os.path.basename(file)}")

    def on_incomplete_file_sent(self, file):
        log(f"Download interrompido: {os.path.basename(file)}")

    def on_incomplete_file_received(self, file):
        log(f"Upload interrompido: {os.path.basename(file)}")


def iniciar_servidor():

    global server

    pasta = entrada_pasta.get()
    usuario = entrada_usuario.get()
    senha = entrada_senha.get()
    porta = int(entrada_porta.get())

    if not os.path.exists(pasta):
        messagebox.showerror("Erro", "Pasta inválida")
        return

    try:

        authorizer = DummyAuthorizer()
        authorizer.add_user(usuario, senha, pasta, perm="elradfmw")

        handler = MeuFTPHandler
        handler.authorizer = authorizer

        server = FTPServer(("0.0.0.0", porta), handler)

        log("Servidor iniciado")
        log(f"Porta: {porta}")
        log(f"Pasta: {pasta}")

        server.serve_forever()

    except Exception as e:
        log(f"Erro: {e}")


def iniciar_thread():
    t = threading.Thread(target=iniciar_servidor)
    t.daemon = True
    t.start()


def parar_servidor():

    global server

    if server:
        server.close_all()
        server = None
        log("Servidor parado")


def encerrar_programa():

    global server

    if server:
        server.close_all()

    janela.destroy()


def escolher_pasta():

    pasta = filedialog.askdirectory()

    if pasta:
        entrada_pasta.delete(0, tk.END)
        entrada_pasta.insert(0, pasta)


def log(msg):

    caixa_log.config(state="normal")
    caixa_log.insert(tk.END, msg + "\n")
    caixa_log.see(tk.END)
    caixa_log.config(state="disabled")


def enter_iniciar(event):
    iniciar_thread()


janela = tk.Tk()
janela.title("Servidor FTP")
janela.geometry("500x500")

tk.Label(janela, text="Usuário").pack()
entrada_usuario = tk.Entry(janela)
entrada_usuario.pack()

tk.Label(janela, text="Senha").pack()
entrada_senha = tk.Entry(janela)
entrada_senha.pack()

tk.Label(janela, text="Porta").pack()
entrada_porta = tk.Entry(janela)
entrada_porta.insert(0, "2121")
entrada_porta.pack()

tk.Label(janela, text="Pasta FTP").pack()

frame_pasta = tk.Frame(janela)
frame_pasta.pack()

entrada_pasta = tk.Entry(frame_pasta, width=35)
entrada_pasta.pack(side=tk.LEFT)

btn_pasta = tk.Button(frame_pasta, text="Selecionar", command=escolher_pasta)
btn_pasta.pack(side=tk.LEFT)

tk.Button(janela, text="Iniciar Servidor", command=iniciar_thread).pack(pady=10)

tk.Button(janela, text="Parar Servidor", command=parar_servidor).pack()

tk.Button(janela, text="Encerrar Programa", command=encerrar_programa, bg="red", fg="white").pack(pady=10)

caixa_log = tk.Text(janela, height=15, state="disabled")
caixa_log.pack(fill="both", expand=True)

janela.bind("<Return>", enter_iniciar)

janela.mainloop()