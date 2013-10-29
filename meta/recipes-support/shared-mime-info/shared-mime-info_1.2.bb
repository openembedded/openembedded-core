require shared-mime-info.inc

SRC_URI[md5sum] = "8f90f3f2b8478fa47e70678d34013f99"
SRC_URI[sha256sum] = "a5516ae241b95a948a6749b1cbf65dc20c3bb563a73cc7bedb7065933788bb78"

SRC_URI =+ "file://parallelmake.patch \
	    file://install-data-hook.patch"
