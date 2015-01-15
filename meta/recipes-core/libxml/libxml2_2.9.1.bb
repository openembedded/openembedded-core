require libxml2.inc

SRC_URI += "http://www.w3.org/XML/Test/xmlts20080827.tar.gz;name=testtar \
            file://72a46a519ce7326d9a00f0b6a7f2a8e958cd1675.patch"

SRC_URI[libtar.md5sum] = "9c0cfef285d5c4a5c80d00904ddab380"
SRC_URI[libtar.sha256sum] = "fd3c64cb66f2c4ea27e934d275904d92cec494a8e8405613780cbc8a71680fdb"
SRC_URI[testtar.md5sum] = "ae3d1ebe000a3972afa104ca7f0e1b4a"
SRC_URI[testtar.sha256sum] = "96151685cec997e1f9f3387e3626d61e6284d4d6e66e0e440c209286c03e9cc7"
