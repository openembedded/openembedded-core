SUMMARY = "LibYAML is a YAML 1.1 parser and emitter written in C."
DESCRIPTION = "LibYAML is a C library for parsing and emitting data in YAML 1.1, \
a human-readable data serialization format. "
HOMEPAGE = "https://pyyaml.org/wiki/LibYAML"
SECTION = "libs/devel"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://License;md5=7bbd28caa69f81f5cd5f48647236663d"

SRC_URI = "https://pyyaml.org/download/libyaml/yaml-${PV}.tar.gz"
SRC_URI[md5sum] = "0532bb32548ee92f1c0328aa8a87dec7"
SRC_URI[sha256sum] = "d80aeda8747b7c26fbbfd87ab687786e58394a8435ae3970e79cb97882e30557"

S = "${WORKDIR}/yaml-${PV}"

inherit autotools

BBCLASSEXTEND = "native nativesdk"
