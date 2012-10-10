require xserver-xorg.inc

# Misc build failure for master HEAD
SRC_URI += "file://crosscompile.patch \
            file://fix_open_max_preprocessor_error.patch \
            file://mips64-compiler.patch \
            file://pkgconfig-deps.patch \
           "

SRC_URI[md5sum] = "256325e9b17dff479d92bed97f6b0adb"
SRC_URI[sha256sum] = "3e2935bc400612df58d5b5e6840829e2c63af02c2e7d2893092500358a4366fc"

PR = "${INC_PR}.0"
