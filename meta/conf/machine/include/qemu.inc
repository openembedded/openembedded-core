PCMCIA_MANAGER = "pcmciautils"
PREFERRED_PROVIDER_virtual/xserver ?= "xserver-kdrive"
PREFERRED_PROVIDER_virtual/libgl ?= "mesa-xlib"

MACHINE_FEATURES = "apm alsa pcmcia bluetooth irda usbgadget screen"

IMAGE_FSTYPES += "tar.bz2 ext3"

ROOT_FLASH_SIZE = "280"

# Don't include kernels in standard images
RDEPENDS_kernel-base = ""

# Use a common kernel recipe for all QEMU machines
PREFERRED_PROVIDER_virtual/kernel ?= "linux-yocto"

EXTRA_IMAGEDEPENDS += "qemu-native qemu-helper-native"
