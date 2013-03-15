from __future__ import with_statement
from contextlib import closing
import sqlite3
from flask import Flask, request, session, g, redirect, url_for, \
     abort, render_template, flash

# config
DATABASE = '/tmp/club.db'
DEBUG = True
SECRET_KEY = 'devkey'
USERNAME = 'admin'
PASSWORD = 'default'

app = Flask(__name__)
app.config.from_object(__name__)
app.config.from_envvar('CLUB_SETTINGS', silent=True)

def connect_db():
  return sqlite3.connect(app.config['DATABASE'])

def init_db():
  with closing(connect_db()) as db:
    with app.open_resource('schema.sql') as f:
      db.cursor().executescript(f.read())
    db.commit()


@app.before_request
def before_request():
  g.db = connect_db()

@app.teardown_request
def teardown_request(exception):
  g.db.close()


#------------------------------------------------------------------------



def query_db(query, args=(), one=False):
  print 'columns: ',
  g.db.execute('PRAGMA table_info(clubs)', ())
  cur = g.db.execute(query, args)
  print cur
  rv = [dict(idnum=row[0], name=row[1]) 
             for row in cur.fetchall()]
  return (rv[0] if rv else None) if one else rv 

@app.route('/', methods=['POST', 'GET'])
def show_clubs():
  clubs = query_db('select id, name from clubs order by id desc')
  outstr = '{"names":['
  for club in clubs:
    name = club['name']
    outstr += '"' + name + '",'
  outstr = outstr[:-1] + ']}'
  return outstr
  
def add_entry_manual(name):
  with app.app_context():
    query_db('insert into clubs (name) values (?)', [name])


@app.route('/add', methods=['POST'])
def add_entry():
  with app.app_context():
    print 'clubs: '
    print request.form.get('name', None), request.form.get('password', None)
    clubs = query_db('insert into clubs (name, password) values (?, ?)',
                  [request.form['name'], request.form['password']])
    print 'got clubs: '
    print clubs
    outstr = '{"names":['
    for club in clubs:
      name = club['name']
      outstr += '"' + name + '",'
    outstr = outstr[:-1] + ']}'
    return outstr


app.debug = True

if __name__ == '__main__':
  app.run(host='0.0.0.0')



