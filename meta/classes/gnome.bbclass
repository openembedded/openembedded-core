inherit gnomebase gtk-icon-cache gconf mime

EXTRA_OECONF += "--disable-introspection"

UNKNOWN_CONFIGURE_WHITELIST += "--disable-introspection"
