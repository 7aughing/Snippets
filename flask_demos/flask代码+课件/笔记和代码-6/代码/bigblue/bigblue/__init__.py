from flask import Flask

def create_app():
    app = Flask(__name__)
    app.config.from_object('config.settings')

    from .account import account
    from .admin import admin

    app.register_blueprint(account)
    app.register_blueprint(admin)
    return app