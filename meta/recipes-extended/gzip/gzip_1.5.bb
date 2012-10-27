require gzip.inc

PR = "r0"

PROVIDES_append_class-native = " gzip-replacement-native"
NATIVE_PACKAGE_PATH_SUFFIX = "/${PN}"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "11b76536941ee1c0221fe6eefbcb32cb"
SRC_URI[sha256sum] = "b5d56e8ffc9918e8c941fab56e04121194f9870adeeb859e09c09eac264035a3"
