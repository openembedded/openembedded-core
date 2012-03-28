require qt4-tools-nativesdk.inc

PR = "${INC_PR}.0"

SRC_URI += "file://qmake_pri_fixes.patch"

DEFAULT_PREFERENCE = "-1"

SRC_URI[md5sum] = "7960ba8e18ca31f0c6e4895a312f92ff"
SRC_URI[sha256sum] = "ef851a36aa41b4ad7a3e4c96ca27eaed2a629a6d2fa06c20f072118caed87ae8"
