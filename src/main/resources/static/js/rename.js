
document.addEventListener('DOMContentLoaded', () => {
    const modals = [
        {
            modalId: 'renameFileModal',
            newNameInputId: 'newFileNameInput',
            locationInputId: 'fileLocationInput',
            oldNameInputId: 'oldFileNameInput',
            dataOldNameAttr: 'data-fileOldName',
            dataLocationAttr: 'data-fileLocation'
        },
        {
            modalId: 'renameFolderModal',
            newNameInputId: 'newFolderNameInput',
            locationInputId: 'folderLocationInput',
            oldNameInputId: 'oldFolderNameInput',
            dataOldNameAttr: 'data-folderOldName',
            dataLocationAttr: 'data-folderLocation'
        }
    ];

    modals.forEach(({ modalId, newNameInputId, locationInputId, oldNameInputId, dataOldNameAttr, dataLocationAttr }) => {
        let renameModal = document.getElementById(modalId);
        let newNameInput = document.getElementById(newNameInputId);
        let locationInput = document.getElementById(locationInputId);
        let oldNameInput = document.getElementById(oldNameInputId);

        if (renameModal && newNameInput && locationInput && oldNameInput) {
            renameModal.addEventListener('show.bs.modal', (e) => {
                let renameBtn = e.relatedTarget;
                oldNameInput.value = renameBtn.getAttribute(dataOldNameAttr);
                newNameInput.value = renameBtn.getAttribute(dataOldNameAttr);
                locationInput.value = renameBtn.getAttribute(dataLocationAttr);
            });
        }
    });
});
