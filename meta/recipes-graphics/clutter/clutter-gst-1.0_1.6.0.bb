require clutter-gst-1.0.inc

SRC_URI += "file://cogl-1.14.patch"

LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c \
                    file://clutter-gst/clutter-gst.h;beginline=1;endline=24;md5=95baacba194e814c110ea3bdf25ddbf4"

SRC_URI[archive.md5sum] = "d60ab0118730cecd5dd9988ad7da034b"
SRC_URI[archive.sha256sum] = "abc879cdd562f1640a825131405f4327a427bfe65b805ebc25d0c78909c8c622"
