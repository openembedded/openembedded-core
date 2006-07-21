EXTRA_OECONF = "--with-kerneldir=${STAGING_KERNEL_DIR} --with-driver=${DRIVER}"

DRIVER ?= "serial"
DRIVER_collie = "sa1100"
DRIVER_h3600 = "sa1100"
DRIVER_simpad = "sa1100"
DRIVER_nslu2 = "nslu2"
# XXX Why is X disabled on all these platforms anyway?
EXTRA_OECONF_append_collie = " --without-x"
EXTRA_OECONF_append_h3600 = " --without-x"
EXTRA_OECONF_append_simpad = " --without-x"
EXTRA_OECONF_append_nslu2 = " --without-x --with-transmitter --with-soft-carrier"
