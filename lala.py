data = [
"2019-09-18T06:14:42",
"2019-10-13T13:31:01",
"2019-10-31T11:48:22",
"2019-11-05T17:02:11",
"2019-11-17T14:08:04",
"2019-11-24T01:35:41",
"2019-12-06T12:22:15",
"2019-12-13T02:48:32",
"2020-02-10T09:50:52",
"2020-02-26T21:44:17",
"2020-02-29T07:07:17",
"2020-03-04T05:15:21",
"2020-03-09T09:30:28",
"2020-03-31T21:34:59",
"2020-04-19T05:30:54",
"2020-05-10T06:24:31",
"2020-07-06T07:57:49",
"2020-07-22T03:18:35",
"2020-07-30T07:35:05",
"2020-10-20T17:25:33",
"2020-12-21T10:19:23",
"2021-01-26T22:52:57",
"2021-02-12T06:54:43",
"2021-03-31T05:59:16",
"2021-04-05T08:47:21",
"2021-05-02T09:39:50",
"2021-06-05T05:36:33",
"2021-06-18T01:25:20",
"2021-07-08T11:42:22",
"2021-08-04T21:54:01",
"2021-09-04T12:45:37",
"2021-09-07T03:45:36",
"2021-10-09T10:36:19",
"2021-10-16T05:16:37",
"2021-11-11T11:34:15",
"2021-11-27T17:11:14",
"2022-01-02T01:03:39",
"2022-01-30T13:28:48",
"2022-02-06T13:39:29",
"2022-02-12T10:05:17",
"2022-02-20T09:33:53",
"2022-02-23T14:42:04",
"2022-04-18T10:33:45",
"2022-04-28T04:48:12",
"2022-05-27T16:19:11",
"2022-05-28T06:31:56",
"2022-06-16T20:04:20",
"2022-07-26T23:27:24",
"2022-08-20T15:08:23",
"2022-09-06T20:38:07"
]

import random

for i in range(len(data)):
    print (data[i] + " " + random.choice(data[i+1:]))


