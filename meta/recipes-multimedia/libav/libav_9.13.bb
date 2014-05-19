require libav.inc

LIC_FILES_CHKSUM = "file://COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LGPLv2.1;md5=bd7a443320af8c812e4c18d1b79df004 \
                    file://COPYING.LGPLv3;md5=e6a600fd5e1d9cbde2d983680233ad02"

SRC_URI = "http://libav.org/releases/${BP}.tar.xz"

SRC_URI[md5sum] = "23b9e34bffdaee366710fdf20157a570"
SRC_URI[sha256sum] = "2ff05df6cd2259b3bb277eb16c234214f8e0530700d0c774d033eba23edde6ca"

DEFAULT_PREFERENCE = "-1"
