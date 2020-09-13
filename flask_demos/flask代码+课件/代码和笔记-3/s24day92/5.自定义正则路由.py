from flask import Flask,render_template,views

app = Flask(__name__)
from werkzeug.routing import BaseConverter


class RegConverter(BaseConverter):
    def __init__(self, map, regex):
        super().__init__(map)
        self.regex = regex
app.url_map.converters['regex'] = RegConverter

@app.route('/index/<regex("\d+"):x1>')
def index(x1):
    return render_template('index.html')

if __name__ == '__main__':
    app.run()