#
# Default virtual providers
#
PREFERRED_PROVIDER_virtual/db ?= "db"
PREFERRED_PROVIDER_virtual/db-native ?= "db-native"
PREFERRED_PROVIDER_virtual/xserver ?= "xserver-xorg"
PREFERRED_PROVIDER_virtual/xserver-xf86 ?= "xserver-xorg"
PREFERRED_PROVIDER_virtual/libgl ?= "mesa-dri"
PREFERRED_PROVIDER_virtual/update-alternatives ?= "update-alternatives-cworth"
PREFERRED_PROVIDER_virtual/update-alternatives-native ?= "opkg-native"
PREFERRED_PROVIDER_virtual/libx11 ?= "libx11-trim"
PREFERRED_PROVIDER_xf86-video-intel ?= "xf86-video-intel"

#
# Default virtual runtime providers
#
VIRTUAL-RUNTIME_update-alternatives ?= "update-alternatives-cworth"
VIRTUAL-RUNTIME_apm ?= "apm"
VIRTUAL-RUNTIME_alsa-state ?= "alsa-state"

#
# Default recipe providers
#
PREFERRED_PROVIDER_dbus-glib ?= "dbus-glib"
PREFERRED_PROVIDER_dbus-glib-native ?= "dbus-glib-native"
PREFERRED_PROVIDER_gdk-pixbuf ?= "gdk-pixbuf"
PREFERRED_PROVIDER_libgcc ?= "libgcc"
PREFERRED_PROVIDER_libgcc-nativesdk ?= "libgcc-nativesdk"
PREFERRED_PROVIDER_linux-libc-headers ?= "linux-libc-headers"
PREFERRED_PROVIDER_linux-libc-headers-nativesdk ?= "linux-libc-headers-nativesdk"
PREFERRED_PROVIDER_matchbox-panel ?= "matchbox-panel-2"
PREFERRED_PROVIDER_opkg ?= "opkg"
PREFERRED_PROVIDER_opkg-native ?= "opkg-native"
PREFERRED_PROVIDER_opkg-nativesdk ?= "opkg-nativesdk"
PREFERRED_PROVIDER_console-tools ?= "kbd"
PREFERRED_PROVIDER_gzip-native ?= "pigz-native"
