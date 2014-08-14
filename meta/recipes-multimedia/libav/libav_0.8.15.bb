require libav.inc

SRC_URI = "http://libav.org/releases/${BP}.tar.xz \
           file://0001-configure-enable-pic-for-AArch64.patch"

SRC_URI[md5sum] = "e483ea8f482b5ecd65ee1d09251b0a5b"
SRC_URI[sha256sum] = "495789ec547e93632937f0f36b06f4dd3180bc61518181f124af6746c6218710"

LIC_FILES_CHKSUM = "file://COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LGPLv2.1;md5=e344c8fa836c3a41c4cbd79d7bd3a379 \
                    file://COPYING.LGPLv3;md5=e6a600fd5e1d9cbde2d983680233ad02"

PROVIDES += "libpostproc"

EXTRA_OECONF += " \
    --enable-postproc \
"
