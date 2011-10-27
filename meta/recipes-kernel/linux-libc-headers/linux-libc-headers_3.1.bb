require linux-libc-headers.inc

PR = "r1"

SRC_URI += " file://connector-msg-size-fix.patch"
SRC_URI[md5sum] = "8d43453f8159b2332ad410b19d86a931"
SRC_URI[sha256sum] = "2573d2378c754b0c602b57586e9311e5b38c5d1e6c137f02873833633a4b9359"
