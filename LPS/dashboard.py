from flask import Flask, render_template,request
import plotly
import plotly.graph_objs as go

import pandas as pd
import numpy as np
import json

def load_data():
    df_cities = pd.read_excel('data/xlsx_painel.xlsx')
    df_cities = df_cities.iloc[:, 0:3]
    df_cities['DATA'] = pd.to_datetime(df_cities['DATA'], format = '%d/%m/%Y')
    df_cities = df_cities.sort_values(by = ['DATA'])
    df_total = df_cities.groupby(by=['MUNICIPIO_RESIDENCIA','DATA'])['NUM_CASOS'].sum().groupby(level='MUNICIPIO_RESIDENCIA').cumsum().reset_index(name='TOTAL')
    df_cities['MA'] = df_cities['NUM_CASOS'].rolling(window=7).mean()
    print('LOADED')

    return df_total

app = Flask(__name__)
df_cities_global = load_data()

@app.route('/')
def index():
    feature = 'BELO HORIZONTE'
    print(feature)
    graph = create_plot(feature, df_cities_global)
    return render_template('index.html', plot = graph)

def plot_data(df_cities, cities = ['BELO HORIZONTE', 'DIAMANTINA', 'JUIZ DE FORA', 'CONTAGEM', 'BETIM']):
  data = []
  
  for city in cities:
    df_cities_plot = df_cities[df_cities['MUNICIPIO_RESIDENCIA'] == city]
    data.append(go.Scatter(x = df_cities_plot['DATA'], y=df_cities_plot["TOTAL"], name = city))

  return data

def create_plot(feature, df_cities):
   
    data = plot_data(df_cities, cities = [feature])
   
    print(feature)
    graphJSON = json.dumps(data, cls=plotly.utils.PlotlyJSONEncoder)

    return graphJSON

@app.route('/bar', methods=['GET', 'POST'])
def change_features():

    feature = request.args['selected']
    print(feature)
    graphJSON= create_plot(feature, df_cities_global)

    return graphJSON

if __name__ == '__main__':
    app.run(debug=True)
    app.config['SEND_FILE_MAX_AGE_DEFAULT'] = 0