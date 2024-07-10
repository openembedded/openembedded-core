SUMMARY = "Python package for providing Mozilla's CA Bundle."
DESCRIPTION = "This installable Python package contains a CA Bundle that you can reference in your \
Python code. This is useful for verifying HTTP requests, for example.  This is the same CA Bundle \
which ships with the Requests codebase, and is derived from Mozilla Firefox's canonical set."
HOMEPAGE = " http://certifi.io/"

LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://LICENSE;md5=11618cb6a975948679286b1211bd573c"

SRC_URI[sha256sum] = "5a1e7645bc0ec61a09e26c36f6106dd4cf40c6db3a1fb6352b0244e7fb057c7b"

inherit pypi setuptools3

BBCLASSEXTEND = "native nativesdk"

RDEPENDS:${PN} += "python3-io"
