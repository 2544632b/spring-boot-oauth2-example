import cv2
import urllib
import urllib.parse
import urllib.request
import matplotlib.pyplot as plt
import numpy as np
from sklearn.cluster import KMeans

import warnings
warnings.filterwarnings('ignore', category=FutureWarning)

def centroid_histogram(clt):
    numLabels = np.arange(0, len(np.unique(clt.labels_)) + 1)
    (hist, _) = np.histogram(clt.labels_, bins = numLabels)

    hist = hist.astype("float")
    hist /= hist.sum()

    return hist

def plot_colors(hist, centroids):
    bar = np.zeros((1, 300, 3), dtype = "uint8")
    startX = 0

    for(percent, color) in zip(hist, centroids):
        endX = startX + (percent * 300)
        cv2.rectangle(bar, (int(startX), 0), (int(endX), 50), color.astype("uint8").tolist(), -1)
        startX = endX

    return bar


def get_color(path):    # Publicly for javascript
    response = urllib.request.urlopen(path)

    image = np.asarray(bytearray(response.read()), dtype="uint8")
    image = cv2.imdecode(image, cv2.IMREAD_COLOR)
    # image = cv2.imread(image, 17)
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

    image = image.reshape((image.shape[0] * image.shape[1], 3))

    clt = KMeans(5)
    clt.fit(image)

    hist = centroid_histogram(clt)
    bar = plot_colors(hist, clt.cluster_centers_)

    pixel = [0, 0, 0]
    targetPixel = [0, 0, 0]

    for i in bar:   # ????????
        for j in i:     # Best match in historgram (1*m)
            if pixel[0] == 0 and pixel[1] == 0 and pixel[2] == 0:
                pixel[0] = j[0]
                pixel[1] = j[1]
                pixel[2] = j[2]

            if pixel[0] == j[0] and pixel[1] == j[1] and pixel[2] == j[2]:
                continue
            
            if pixel[0] != j[0] or pixel[1] != j[1] or pixel[2] != j[2]:
                brightness = 0.3 * pixel[0] + 0.6 * pixel[1] + 0.1 * pixel[2]
                targetBrightness = 0.3 * j[0] + 0.6 * j[1] + 0.1 * j[2]
                if targetBrightness >= brightness:
                    continue
                else:
                    targetPixel = j    # Given a pixel

                break

    return targetPixel

    # plt.figure()
    # plt.axis("off")
    # plt.imshow(bar)
    # plt.show()

print(get_color("https://www.wenjiangs.com/wp-content/uploads/2020/03/scikit-learn-logo.jpg"))