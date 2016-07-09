require libxml2.inc

SRC_URI += "http://www.w3.org/XML/Test/xmlts20080827.tar.gz;name=testtar \
            file://72a46a519ce7326d9a00f0b6a7f2a8e958cd1675.patch \
	    file://0001-threads-Define-pthread-definitions-for-glibc-complia.patch \
	   "
SRC_URI += "file://CVE-2016-1762.patch \
            file://CVE-2016-3705.patch \
            file://CVE-2016-1834.patch \
            file://CVE-2016-4483.patch \
            file://CVE-2016-1840.patch \
            file://CVE-2016-1838.patch \
            file://CVE-2016-1839.patch \
            file://CVE-2016-1836.patch \
            file://CVE-2016-4449.patch \
            file://CVE-2016-1837.patch \
            file://CVE-2016-1835.patch \
            file://CVE-2016-1833.patch \
            file://CVE-2016-3627.patch \
            file://CVE-2016-4447.patch \
            file://CVE-2016-4448_1.patch \
            file://CVE-2016-4448_2.patch \
    "

SRC_URI[libtar.md5sum] = "9e6a9aca9d155737868b3dc5fd82f788"
SRC_URI[libtar.sha256sum] = "5178c30b151d044aefb1b08bf54c3003a0ac55c59c866763997529d60770d5bc"
SRC_URI[testtar.md5sum] = "ae3d1ebe000a3972afa104ca7f0e1b4a"
SRC_URI[testtar.sha256sum] = "96151685cec997e1f9f3387e3626d61e6284d4d6e66e0e440c209286c03e9cc7"
