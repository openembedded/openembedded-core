SUMMARY = "Setuptools extension for CalVer package versions"
HOMEPAGE = "https://github.com/di/calver"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://github.com/di/calver;branch=master;protocol=https;tag=${PV}"
SRCREV = "5b62e5127f9ad5bb56bd09ccd8c44283a23ddb60"

inherit python_setuptools_build_meta ptest-python-pytest

S = "${WORKDIR}/git"

RDEPENDS:${PN}-ptest += " \
    python3-pretend \
"

BBCLASSEXTEND = "native nativesdk"
