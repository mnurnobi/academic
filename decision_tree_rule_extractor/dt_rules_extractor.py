
from numpy import loadtxt
import numpy as np
from keras.utils import to_categorical

import matplotlib.pyplot as plt
# Importing standardscalar module
from sklearn.preprocessing import StandardScaler
# Importing PCA
from sklearn.decomposition import PCA
import pandas as pd

from sklearn import svm
from sklearn.model_selection import train_test_split
from sklearn.metrics import confusion_matrix, classification_report
from sklearn import preprocessing
from sklearn.metrics import accuracy_score
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier



#dataFileName = 'tiny.sample'
dataFileName = 'UR_15_1200_1200.sample'

# load the dataset
raw_dataset = loadtxt(dataFileName, delimiter=' ', dtype=np.str)
dataset = raw_dataset[:,0:20]
#dataset = raw_dataset[0:200,0:45]
flattend_UR_pair = []

np.random.shuffle(dataset)

# split into user, resource and operations variables
#user = dataset[:,0:8]
#resource = dataset[:,8:16]
ur_pair = dataset[:,0:16]
operations = dataset[:,16:20]

# applying Keras one-hot encoding
#encoded_ur_pair = to_categorical(ur_pair)

pairs = ur_pair.shape[0]
print(pairs)
df = pd.DataFrame([])
df_label = pd.DataFrame([])

for i in range(pairs):
	df_label = df_label.append(pd.Series(operations[i][0]+operations[i][1]+operations[i][2]+operations[i][3]),  ignore_index=True, verify_integrity=False, sort=None)

#df = ur_pair
#df_label = operations
print(ur_pair[0])
print(df.shape)
print(df_label.shape)

df = ur_pair
df = df.astype(np.float64)

X_Train, X_Test, Y_Train, Y_Test = train_test_split(df, df_label, test_size=0.25)

# decision tree model creation
classifier = DecisionTreeClassifier(criterion="entropy", max_depth=7)
#classifier = DecisionTreeClassifier(criterion="entropy")
#classifier = DecisionTreeClassifier()
classifier = classifier.fit(X_Train, Y_Train)
y_pred = classifier.predict(X_Test)

print(confusion_matrix(Y_Test, y_pred))
print(classification_report(Y_Test, y_pred))

print("Accuracy Score :")
print(accuracy_score(Y_Test, y_pred))

from sklearn.tree import _tree
#feature = estimator.tree_.feature

def find_rules(tree, features):
    dt = tree.tree_
    feature = dt.feature
    def visitor(node, depth):
        indent = ' ' * depth
        if dt.feature[node] != _tree.TREE_UNDEFINED:
            attrib_type = 'uattr_' if dt.feature[node] < 8 else 'rattr_' 
            feature_name = attrib_type + str(dt.feature[node]%8)
            print('{}if <{}> <= {}:'.format(indent, feature_name, round(dt.threshold[node], 2)))
            #print('{}if <{}> <= {}:'.format(indent, features[node], round(dt.threshold[node], 2)))
            visitor(dt.children_left[node], depth + 1)
            print('{}else:'.format(indent))
            visitor(dt.children_right[node], depth + 1)
        else:
            idx = np.argmax(dt.value[node])
            cls = 'class_' + str(idx)
            print('{}return {}'.format(indent, cls))
            #print(cls + '{}return {}'.format(indent, dt.value[node]))
    
    visitor(0, 1)

features = ["uattr0","uattr1","uattr2","uattr3","uattr4","uattr5","uattr6","uattr7","rattr0","rattr1","rattr2","rattr3","rattr4","rattr5","rattr6","rattr7", "EXTRA", "ex2", "ex3"]
find_rules( classifier, features )

