SUMMARY = "LibYAML is a YAML 1.1 parser and emitter written in C."
DESCRIPTION = "LibYAML is a C library for parsing and emitting data in YAML 1.1, \
a human-readable data serialization format. "
HOMEPAGE = "http://pyyaml.org/wiki/LibYAML"
SECTION = "libs/devel"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a76b4c69bfcf82313bbdc0393b04438a"

SRC_URI = "http://pyyaml.org/download/libyaml/yaml-${PV}.tar.gz"
SRC_URI[md5sum] = "2ad4119a57f94739cc39a1b482c81264"
SRC_URI[sha256sum] = "46bca77dc8be954686cff21888d6ce10ca4016b360ae1f56962e6882a17aa1fe"

inherit autotools

BBCLASSEXTEND = "native nativesdk"
