from flask import Flask, render_template, request, redirect

app = Flask(__name__)

DATA_LIST = {
    1: {"name": "xm", "age": 15},
    2: {"name": "zs", "age": 16},
    3: {"name": "hy", "age": 17},
}


@app.route("/login", methods=["GET", "POST"])
def login():
    if request.method == "GET":
        return render_template("login.html")
    
    user = request.form.get("user")
    pwd = request.form.get("pwd")

    if user == "abc" and pwd == "abc":
        return redirect("index")
    
    errmsg = "登陆失败"
    return render_template("login.html", error = errmsg)


@app.route("/index", endpoint="idx")
def index():

    # 传递数据到前端页面
    data_dict = DATA_LIST
    return render_template("index.html", data_dict=data_dict)


@app.route("/del/<int:nid>")
def delete(nid):
    del DATA_LIST[nid]
    return redirect("/index")

@app.route("/edit", methods=["GET", "POST"])
def edit():
    nid = int(request.args.get("nid"))

    if request.method=="GET":
        info = DATA_LIST[nid]
        return render_template("edit.html", info=info)
    else:

        name = request.form.get("name")
        age = request.form.get("age")
        DATA_LIST[nid]["name"] = name 
        DATA_LIST[nid]["age"] = age

        # return redirect("/index")             # 以url的方式进行重定向
        return redirect(url_for("idx"))         # 以别名的方式进行重定向



if __name__ == "__main__":
    
    app.run()
