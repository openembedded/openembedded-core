# Misc build failure for master HEAD
SRC_URI += "file://crosscompile.patch \
            file://fix_open_max_preprocessor_error.patch \
            file://gcc-47-warning.patch \
           "

SRC_URI[md5sum] = "8796fff441e5435ee36a72579008af24"
SRC_URI[sha256sum] = "fa415decf02027ca278b06254ccfbcceba2a83c2741405257ebf749da4a73cf2"

PR = "r2"

