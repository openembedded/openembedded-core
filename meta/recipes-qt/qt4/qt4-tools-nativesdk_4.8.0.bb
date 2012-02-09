require qt4-tools-nativesdk.inc

PR = "${INC_PR}.0"

SRC_URI += "file://qmake_pri_fixes.patch"

DEFAULT_PREFERENCE = "-1"

SRC_URI[md5sum] = "e8a5fdbeba2927c948d9f477a6abe904"
SRC_URI[sha256sum] = "9392b74e485e15f75a3e07a527547d4f6747eaf55ebce71ba0e863a9fd320b6e"
