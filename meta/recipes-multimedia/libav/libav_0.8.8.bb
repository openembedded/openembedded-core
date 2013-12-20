require libav.inc

SRC_URI = "http://libav.org/releases/libav-0.8.8.tar.xz \
           file://0001-configure-enable-pic-for-AArch64.patch"

SRC_URI[md5sum] = "34b8f1279a04466386ed67731197efe3"
SRC_URI[sha256sum] = "e95cf618eb6239177a62c46f15e840c37e02e8308baf94912fc5910ff4aacbf2"

LIC_FILES_CHKSUM = "file://COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LGPLv2.1;md5=e344c8fa836c3a41c4cbd79d7bd3a379 \
                    file://COPYING.LGPLv3;md5=e6a600fd5e1d9cbde2d983680233ad02"

PROVIDES += "libpostproc"

EXTRA_OECONF += " \
    --enable-postproc \
"


