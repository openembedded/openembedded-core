# Add SOC_FAMILY to machine overrides so we get access to e.g. 'omap3' and 'ti335x'
MACHINEOVERRIDES =. "${@['', '${SOC_FAMILY}:']['${SOC_FAMILY}' != '']}"
