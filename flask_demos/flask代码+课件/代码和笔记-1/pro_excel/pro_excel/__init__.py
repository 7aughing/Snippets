from flask import Flask
from .views.my import xmy
from .views.wy import xwy

def create_app():
    app = Flask(__name__)
    app.secret_key = 'asdfaskdfjsd'

    @app.route('/index')
    def index():
        return 'index'

    app.register_blueprint(xmy)
    app.register_blueprint(xwy)
    # app.register_blueprint(xmy,url_prefix='/web')
    # app.register_blueprint(xwy,url_prefix='/admin')

    return app