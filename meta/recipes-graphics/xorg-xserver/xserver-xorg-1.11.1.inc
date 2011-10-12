SRC_URI += "file://crosscompile.patch"

# Misc build failure for master HEAD
SRC_URI += "file://fix_open_max_preprocessor_error.patch"

PR = "r1"

SRC_URI[md5sum] = "4cdf54dc569b6a5548257c93a45dcd3d"
SRC_URI[sha256sum] = "c069c177d9a80bca2ee1d49aa1af6d9f996c83e4ff12393cab2ca3f8817276f6"
