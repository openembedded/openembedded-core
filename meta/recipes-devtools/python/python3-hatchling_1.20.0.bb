SUMMARY = "The extensible, standards compliant build backend used by Hatch"
HOMEPAGE = "https://hatch.pypa.io/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=cbe2fd33fc9297692812fc94b7d27fd9"

inherit pypi python_hatchling

DEPENDS += "python3-pluggy-native python3-pathspec-native python3-packaging-native python3-editables-native python3-trove-classifiers-native"
DEPENDS:remove:class-native = "python3-hatchling-native"

SRC_URI[sha256sum] = "0e0893cbe3d5f9275fc0e5b629087fc23b17abd7065e4db0a310e0a0237bc945"

do_compile:prepend() {
    export PYTHONPATH=src
}

BBCLASSEXTEND = "native nativesdk"
