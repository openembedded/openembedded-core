require gzip.inc

PROVIDES_append_class-native = " gzip-replacement-native"
NATIVE_PACKAGE_PATH_SUFFIX = "/${PN}"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "38603cb2843bf5681ff41aab3bcd6a20"
SRC_URI[sha256sum] = "97eb83b763d9e5ad35f351fe5517e6b71521d7aac7acf3e3cacdb6b1496d8f7e"
