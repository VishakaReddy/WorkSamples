#!/usr/bin/env python
# coding: utf-8

import numpy as np 
import pandas as pd 
import matplotlib.pyplot as plt
import seaborn as sns

data = pd.read_csv('C:/Users/visha/OneDrive/Documents/TAI/Project/KNNAlgorithmDataset.csv')

data.head()

y=data.filter(['diagnosis'],axis=1)

y.head()

y.value_counts()

X=data.drop(['diagnosis'],axis=1)

X.head()


X=X.drop(['Unnamed: 32'],axis=1)

X.isnull().sum()

def correlation(knn,thresold):
    data=knn.corr()
    corr_features= set()
    for i in range(len(data.columns)):
        for j in range(i):
            if(abs(data.iloc[i,j])>thresold):
                colname=data.columns[i]
                corr_features.add(colname) 
    return corr_features

features=correlation(X,0.8)

features

X =X.drop(features, axis=1)

X=X.drop(['id'],axis=1)
newdata = X

colors = {'M':'r', 'B':'g'}
# create a figure and axis
fig, ax = plt.subplots()
# plot each data-point
for i in range(len(data['radius_mean'])):
    ax.scatter(data['radius_mean'][i], data['texture_mean'][i],color=colors[data['diagnosis'][i]])

newdata.head()
print(newdata.shape)

sns.pairplot(X)

from sklearn.preprocessing import StandardScaler
scaler = StandardScaler()
scaled_features = scaler.fit_transform(X.values)

scaled_features_df = pd.DataFrame(scaled_features, index=X.index, columns=X.columns)

X.head()

scaled_features_df.head()

X=scaled_features_df

X.head()

X.shape

y.shape

from sklearn.neighbors import KNeighborsClassifier
from sklearn.model_selection import train_test_split

X_train, X_test, y_train, y_test = train_test_split(X, y, train_size=0.7,test_size=0.3)

model=KNeighborsClassifier(n_neighbors=5)

model.fit(X_train,y_train.values.ravel())

pred=model.predict(X_test)

from sklearn.metrics import classification_report
print(classification_report(y_test,pred))

from sklearn.metrics import accuracy_score,classification_report,plot_confusion_matrix,confusion_matrix,plot_precision_recall_curve,plot_roc_curve

accuracy_score(y_test,pred)

error= []

for i in range(1,50):
    knn = KNeighborsClassifier(n_neighbors=i)
    knn.fit(X_train,y_train.values.ravel())
    pred = knn.predict(X_test)
    error.append(1-(accuracy_score(y_test,pred)))

error

print("Minimum error:-",min(error),"at K =",error.index(min(error)))
p=error.index(min(error))

model=KNeighborsClassifier(n_neighbors=14)

model.fit(X_train,y_train.values.ravel())

pred=model.predict(X_test)

accuracy_score(y_test,pred)

plot_confusion_matrix(knn,X_test,y_test)