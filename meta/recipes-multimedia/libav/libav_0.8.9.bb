require libav.inc

SRC_URI = "http://libav.org/releases/${BP}.tar.xz \
           file://0001-configure-enable-pic-for-AArch64.patch"

SRC_URI[md5sum] = "7c0e5743cd5c4d4b1bcafd02a2e82e09"
SRC_URI[sha256sum] = "4f5181164e4007d5b0013bb3cc5e3b7393ab71ac286319c714b7697e2784f77f"

LIC_FILES_CHKSUM = "file://COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LGPLv2.1;md5=e344c8fa836c3a41c4cbd79d7bd3a379 \
                    file://COPYING.LGPLv3;md5=e6a600fd5e1d9cbde2d983680233ad02"

PROVIDES += "libpostproc"

EXTRA_OECONF += " \
    --enable-postproc \
"
