def release_dict():
    """Return the output of lsb_release -a as a dictionary"""
    from subprocess import PIPE

    try:
        output, err = bb.process.run(['lsb_release', '-a'], stderr=PIPE)
    except bb.process.CmdError as exc:
        return None

    data = {}
    for line in output.splitlines():
        try:
            key, value = line.split(":\t", 1)
        except ValueError:
            continue
        else:
            data[key] = value
    return data

def distro_identifier(adjust_hook=None):
    """Return a distro identifier string based upon lsb_release -ri,
       with optional adjustment via a hook"""

    lsb_data = release_dict()
    if lsb_data:
        distro_id, release = lsb_data['Distributor ID'], lsb_data['Release']
    else:
        distro_id, release = None, None
        
    if adjust_hook:
        distro_id, release = adjust_hook(distro_id, release)
    if not distro_id:
        return "Unknown"
    return '{0}-{1}'.format(distro_id, release).replace(' ','-').replace('/','-')
