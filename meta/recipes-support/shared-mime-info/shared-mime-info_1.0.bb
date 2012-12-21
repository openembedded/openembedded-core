require shared-mime-info.inc
PR = "r0"

SRC_URI[md5sum] = "901b7977dbb2b71d12d30d4d8fb97028"
SRC_URI[sha256sum] = "d2e830e5aae213dd906e64495e9618cc4ef40d7b249e0971a190b04d5802ae8f"

SRC_URI =+ "file://parallelmake.patch \
	    file://install-data-hook.patch"
