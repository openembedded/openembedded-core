require libav.inc

LIC_FILES_CHKSUM = "file://COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LGPLv2.1;md5=bd7a443320af8c812e4c18d1b79df004 \
                    file://COPYING.LGPLv3;md5=e6a600fd5e1d9cbde2d983680233ad02"

SRC_URI = "http://libav.org/releases/${BP}.tar.xz"

SRC_URI[md5sum] = "7b44b75cec24b8e7545e5029e76917e0"
SRC_URI[sha256sum] = "ca846473b0b8ed8e3404c52e5e92df6d35cb5fa487eec498525de3ffda4367a0"

DEFAULT_PREFERENCE = "-1"
