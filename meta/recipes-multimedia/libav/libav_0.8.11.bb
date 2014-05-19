require libav.inc

SRC_URI = "http://libav.org/releases/${BP}.tar.xz \
           file://0001-configure-enable-pic-for-AArch64.patch"

SRC_URI[md5sum] = "cdc9b53c56a375baf73ea38cf7ade4f9"
SRC_URI[sha256sum] = "5934e4f0dbf6e0fc4987de86cdd079f1d11a1410ae275e9f46472af17f05155a"

LIC_FILES_CHKSUM = "file://COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LGPLv2.1;md5=e344c8fa836c3a41c4cbd79d7bd3a379 \
                    file://COPYING.LGPLv3;md5=e6a600fd5e1d9cbde2d983680233ad02"

PROVIDES += "libpostproc"

EXTRA_OECONF += " \
    --enable-postproc \
"
