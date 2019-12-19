#!/usr/bin/env python
# coding: utf-8

# In[4]:


from datetime import datetime, timedelta
import pandas as pd


# In[5]:


import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import chart_studio.plotly as py
import plotly.offline as pyoff
import plotly.graph_objs as go


# In[6]:


pyoff.init_notebook_mode()
dt = pd.read_csv('OnlineRetail.csv', encoding = 'unicode_escape')
dt.head(5)


# In[7]:


dt['InvoiceDate'] = pd.to_datetime(dt['InvoiceDate'])
dt_uk = dt.query("Country=='United Kingdom'").reset_index(drop=True)


# In[8]:


dt_user = pd.DataFrame(dt['CustomerID'].unique())
dt_user.columns=['CustomerID']

dt_max_purchase = dt_uk.groupby('CustomerID').InvoiceDate.max().reset_index()
dt_max_purchase.columns = ['CustomerID', 'MaxPurchaseDate']

dt_max_purchase['Recency'] = (dt_max_purchase['MaxPurchaseDate'].max() - dt_max_purchase['MaxPurchaseDate']).dt.days
dt_user = pd.merge(dt_user, dt_max_purchase[['CustomerID', 'Recency']], on='CustomerID')
dt_user.head(5)


# In[9]:


plot_data = [
    go.Histogram(
        x=dt_user['Recency']
    )
]
plot_layout = go.Layout(
    title = 'Recency'
)
fig = go.Figure(data = plot_data, layout=plot_layout)
pyoff.iplot(fig)


# In[10]:


dt_user.Recency.describe()


# In[11]:


from sklearn.cluster import KMeans
sse={}
dt_recency = dt_user[['Recency']]
for k in range(1, 10):
    kmeans = KMeans(n_clusters=k, max_iter = 1000).fit(dt_recency)
    dt_recency["clusters"] = kmeans.labels_
    sse[k]=kmeans.inertia_
plt.figure()
plt.plot(list(sse.keys()), list(sse.values()))
plt.xlabel("Number of cluster")
plt.show()


# In[12]:


kmeans = KMeans(n_clusters=4)
kmeans.fit(dt_user[['Recency']])
dt_user['RecencyCluster'] = kmeans.predict(dt_user[['Recency']])

def order_cluster(cluster_field_name, target_field_name, df, ascending):
    new_cluster_field_name = 'new_' + cluster_field_name
    df_new = df.groupby(cluster_field_name)[target_field_name].mean().reset_index()
    df_new = df_new.sort_values(by=target_field_name, ascending=ascending).reset_index(drop=True)
    df_new['index'] = df_new.index
    df_final = pd.merge(df,df_new[[cluster_field_name,'index']], on=cluster_field_name)
    df_final = df_final.drop([cluster_field_name],axis=1)
    df_final = df_final.rename(columns={"index":cluster_field_name})
    return df_final

dt_user = order_cluster('RecencyCluster', 'Recency', dt_user, False)


# In[ ]:





# In[13]:


dt_frequency = dt_uk.groupby('CustomerID').InvoiceDate.count().reset_index()
dt_frequency.columns = ['CustomerID', 'Frequency']
dt_user = pd.merge(dt_user, dt_frequency, on ='CustomerID')

plot_data = [
    go.Histogram(
        x = dt_user.query('Frequency < 1000')['Frequency']
    )
]

plot_layout = go.Layout(title ='Frequency')
fig = go.Figure(data=plot_data, layout=plot_layout)
pyoff.iplot(fig)


# In[14]:


kmeans = KMeans(n_clusters=4)
kmeans.fit(dt_user[['Frequency']])
dt_user['FrequencyCluster'] = kmeans.predict(dt_user[['Frequency']])

dt_user = order_cluster('FrequencyCluster', 'Frequency', dt_user, True)

dt_user.groupby('FrequencyCluster')['Frequency'].describe()


# In[15]:


dt_uk['Revenue'] = dt_uk['UnitPrice'] * dt_uk['Quantity']
dt_revenue = dt_uk.groupby('CustomerID').Revenue.sum().reset_index()
                           

dt_user = pd.merge(dt_user, dt_revenue, on='CustomerID')
                           
plot_data = [
    go.Histogram(
        x = dt_user.query('Revenue < 10000')['Revenue']
        
    )
]
                           
plot_layout = go.Layout(title = 'Monetary value')
fig = go.Figure(data=plot_data, layout = plot_layout)
pyoff.iplot(fig)


# In[17]:


kmeans = KMeans(n_clusters = 4)
kmeans.fit(dt_user[['Revenue']])
dt_user['RevenueCluster'] = kmeans.predict(dt_user[['Revenue']])

dt_user = order_cluster('RevenueCluster', 'Revenue', dt_user, True)

dt_user.groupby('RevenueCluster')['Revenue'].describe()


# In[18]:


dt_user['OverallScore'] = dt_user['RecencyCluster'] + dt_user['FrequencyCluster'] + dt_user['RevenueCluster']
dt_user.groupby('OverallScore')['Recency', 'Frequency', 'Revenue'].mean()


# In[19]:


dt_user['Segment'] = 'Low-Value'
dt_user.loc[dt_user['OverallScore'] > 2, 'Segment'] = 'Mid-Value'
dt_user.loc[dt_user['OverallScore'] > 4, 'Segment'] = 'High-Value'


# In[22]:


dt_graph = dt_user.query("Revenue < 50000 and Frequency < 2000")
plot_data = [
    go.Scatter(
        x = dt_graph.query("Segment == 'Low-Value'")['Frequency'],
        y = dt_graph.query("Segment == 'Low-Value'")['Revenue'],
        mode = 'markers',
        name = "Low",
         marker= dict(size= 7,
            line= dict(width=1),
            color= 'blue',
            opacity= 0.8
           )
        
    ),
     go.Scatter(
        x=dt_graph.query("Segment == 'Mid-Value'")['Frequency'],
        y=dt_graph.query("Segment == 'Mid-Value'")['Revenue'],
        mode='markers',
        name='Mid',
        marker= dict(size= 9,
            line= dict(width=1),
            color= 'green',
            opacity= 0.5
           )
    ),
        go.Scatter(
        x=dt_graph.query("Segment == 'High-Value'")['Frequency'],
        y=dt_graph.query("Segment == 'High-Value'")['Revenue'],
        mode='markers',
        name='High',
        marker= dict(size= 11,
            line= dict(width=1),
            color= 'red',
            opacity= 0.9
           )
    ),
]


# In[23]:


plot_layout = go.Layout(
        yaxis= {'title': "Revenue"},
        xaxis= {'title': "Frequency"},
        title='Segments'
    )
fig = go.Figure(data=plot_data, layout=plot_layout)
pyoff.iplot(fig)


# In[26]:


dt_graph = dt_user.query("Revenue < 50000 and Frequency < 2000")

plot_data = [
    go.Scatter(
        x=dt_graph.query("Segment == 'Low-Value'")['Recency'],
        y=dt_graph.query("Segment == 'Low-Value'")['Revenue'],
        mode='markers',
        name='Low',
        marker= dict(size= 7,
            line= dict(width=1),
            color= 'blue',
            opacity= 0.8
           )
    ),
        go.Scatter(
        x=dt_graph.query("Segment == 'Mid-Value'")['Recency'],
        y=dt_graph.query("Segment == 'Mid-Value'")['Revenue'],
        mode='markers',
        name='Mid',
        marker= dict(size= 9,
            line= dict(width=1),
            color= 'green',
            opacity= 0.5
           )
    ),
        go.Scatter(
        x=dt_graph.query("Segment == 'High-Value'")['Recency'],
        y=dt_graph.query("Segment == 'High-Value'")['Revenue'],
        mode='markers',
        name='High',
        marker= dict(size= 11,
            line= dict(width=1),
            color= 'red',
            opacity= 0.9
           )
    ),
]

plot_layout = go.Layout(
        yaxis= {'title': "Revenue"},
        xaxis= {'title': "Recency"},
        title='Segments'
    )
fig = go.Figure(data=plot_data, layout=plot_layout)
pyoff.iplot(fig)

# Revenue vs Frequency
dt_graph = dt_user.query("Revenue < 50000 and Frequency < 2000")

plot_data = [
    go.Scatter(
        x=dt_graph.query("Segment == 'Low-Value'")['Recency'],
        y=dt_graph.query("Segment == 'Low-Value'")['Frequency'],
        mode='markers',
        name='Low',
        marker= dict(size= 7,
            line= dict(width=1),
            color= 'blue',
            opacity= 0.8
           )
    ),
        go.Scatter(
        x=dt_graph.query("Segment == 'Mid-Value'")['Recency'],
        y=dt_graph.query("Segment == 'Mid-Value'")['Frequency'],
        mode='markers',
        name='Mid',
        marker= dict(size= 9,
            line= dict(width=1),
            color= 'green',
            opacity= 0.5
           )
    ),
        go.Scatter(
        x=dt_graph.query("Segment == 'High-Value'")['Recency'],
        y=dt_graph.query("Segment == 'High-Value'")['Frequency'],
        mode='markers',
        name='High',
        marker= dict(size= 11,
            line= dict(width=1),
            color= 'red',
            opacity= 0.9
           )
    ),
]

plot_layout = go.Layout(
        yaxis= {'title': "Frequency"},
        xaxis= {'title': "Recency"},
        title='Segments'
    )
fig = go.Figure(data=plot_data, layout=plot_layout)
pyoff.iplot(fig)


# In[27]:


dt_user.head(5)


# In[42]:


print(dt_user[['CustomerID'], ['RecencyCluster'], ['FrequencyCluster'], ['RevenueCluster']])


# In[ ]:




