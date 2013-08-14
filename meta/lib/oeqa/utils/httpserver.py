import SimpleHTTPServer
import multiprocessing
import os

class HTTPServer(SimpleHTTPServer.BaseHTTPServer.HTTPServer):

    def server_start(self, root_dir):
        os.chdir(root_dir)
        self.serve_forever()

class HTTPRequestHandler(SimpleHTTPServer.SimpleHTTPRequestHandler):

    def log_message(self, format_str, *args):
        pass

class HTTPService(object):

    def __init__(self, root_dir):
        self.root_dir = root_dir
        self.port = 0

    def start(self):
        self.server = HTTPServer(('', self.port), HTTPRequestHandler)
        if self.port == 0:
            self.port = self.server.server_port
        self.process = multiprocessing.Process(target=self.server.server_start, args=[self.root_dir])
        self.process.start()

    def stop(self):
        self.server.server_close()
        self.process.terminate()
        self.process.join()
