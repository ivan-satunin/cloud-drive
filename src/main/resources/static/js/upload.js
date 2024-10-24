document.addEventListener('DOMContentLoaded', () => {
    const uploadHandlers = [
        { btnId: 'chooseFileBtn', inputId: 'uploadFileInput', formId: 'uploadFileForm' },
        { btnId: 'chooseFolderBtn', inputId: 'uploadFolderInput', formId: 'uploadFolderForm' }
    ];

    uploadHandlers.forEach(handler => {
        const chooseBtn = document.getElementById(handler.btnId);
        const uploadInput = document.getElementById(handler.inputId);
        const uploadForm = document.getElementById(handler.formId);

        if (chooseBtn && uploadInput && uploadForm) {
            chooseBtn.addEventListener('click', () => uploadInput.click());

            uploadInput.addEventListener('change', () => {
                if (uploadInput.files && uploadInput.files.length > 0) {
                    uploadForm.submit();
                }
            });
        }
    });
});
