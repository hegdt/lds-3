import {
  Box,
  FormControl,
  FormGroup,
  Input,
  InputLabel,
  Button,
  CircularProgress,
} from "@mui/material";
import Typography from "@mui/material/Typography";
import Modal from "@mui/material/Modal";
import { useState } from "react";
import { useForm } from "react-hook-form";
import axios from "axios";

const style = {
  position: "absolute",
  alignItems: "center",
  justifyContent: "center",
  textAlign: "center",
  borderRadius: "1rem",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 400,
  bgcolor: "background.paper",
  boxShadow: 24,
  p: 4,
};

export default function MoedaPage() {
  const [openModal, setOpenModal] = useState(false);
  const [alunoParaConfirmacao, setAlunoParaConfirmacao] = useState(null);
  const [isLoadingAluno, setIsLoadingAluno] = useState(false); 
  const [errorAluno, setErrorAluno] = useState(""); 
  const { register, handleSubmit, getValues } = useForm();

  const handlePrepareAndOpenModal = async () => {
    const alunoId = getValues("alunoId");
    const quantidade = getValues("quantidade");

    if (!alunoId || !quantidade) {
      alert("Por favor, preencha o ID do Aluno e a Quantidade.");
      return;
    }
    if (isNaN(parseFloat(quantidade)) || parseFloat(quantidade) <= 0) {
      alert("Por favor, insira uma quantidade válida (maior que zero).");
      return;
    }

    setIsLoadingAluno(true);
    setErrorAluno("");
    setAlunoParaConfirmacao(null); 

    try {
      const response = await fetch(`http://localhost:8080/alunos/${alunoId}`);
      if (!response.ok) {
        let errorMessage = `Aluno com ID '${alunoId}' não encontrado.`;
        try {
          const errorData = await response.json();
          errorMessage = errorData.message || errorMessage;
        } catch (e) {
        }
        setErrorAluno(errorMessage);
        setOpenModal(true); 
        return;
      }
      const alunoData = await response.json();
      setAlunoParaConfirmacao(alunoData);
      setOpenModal(true); 
    } catch (error) {
      console.error("Falha ao buscar aluno:", error);
      const networkErrorMessage = "Falha ao conectar com o servidor para buscar o aluno.";
      setErrorAluno(networkErrorMessage);
      setOpenModal(true); 
    } finally {
      setIsLoadingAluno(false);
    }
  };

  const handleCloseModal = () => {
    setOpenModal(false);
  };

  const processarEnvioMoedas = (dataDoFormulario) => {
    if (!alunoParaConfirmacao || !alunoParaConfirmacao.id || errorAluno) {
      alert("Não é possível enviar moedas. Verifique os dados do aluno.");
      handleCloseModal();
      return;
    }

    const quantidadeParaEnviar = dataDoFormulario.quantidade;

    try {
      axios
        .post(
          "http://localhost:8080/professores/enviarMoedas",
          {
            professorId: localStorage.getItem("idUser"),
            alunoId: alunoParaConfirmacao.id, 
            quantidade: quantidadeParaEnviar
          },
          {
            headers: { "Content-Type": "application/json" },
          }
        )
        .then((res) => {
          if (res.status === 200) {
            alert("Moedas enviadas com sucesso!");
            handleCloseModal();
          } else {
            alert(`Erro ao enviar moedas: Status ${res.status}`);
          }
        })
        .catch((error) => {
          console.error("Erro no envio de moedas:", error.response?.data || error.message);
          const errorMessage = error.response?.data?.message || error.response?.data || "Falha ao enviar moedas. Verifique os dados e tente novamente.";
          alert(errorMessage);
        });
    } catch (error) {
      console.error("Erro inesperado ao processar envio de moedas:", error);
      alert("Ocorreu um erro inesperado.");
    }
  };

  const labels = [
    { id: "alunoId", label: "ID do Aluno", type: "text", rules: { required: "ID do Aluno é obrigatório" } },
    { id: "quantidade", label: "Quantidade de Moedas", type: "number", rules: { required: "Quantidade é obrigatória", min: { value: 1, message: "Quantidade deve ser maior que 0" } } },
  ];

  return (
    <Box sx={{ width: "100%" }}>
      <Box
        sx={{
          display: "grid",
          alignItems: "center",
          justifyContent: "center",
          textAlign: "center",
        }}
      >
        <Typography gutterBottom variant="h5" component="h1">
          Enviar moedas
        </Typography>

        <FormGroup>
          {labels.map((field) => (
            <FormControl key={field.id} className="field-container" sx={{ my: 1 }}>
              <InputLabel htmlFor={field.id}>{field.label}</InputLabel>
              <Input
                type={field.type}
                id={field.id}
                name={field.id}
                {...register(field.id, field.rules)}
              />
            </FormControl>
          ))}
          <Button
            color="primary"
            variant="outlined"
            sx={{ marginTop: "10px" }}
            onClick={handlePrepareAndOpenModal} 
            disabled={isLoadingAluno}
          >
            {isLoadingAluno ? <CircularProgress size={24} /> : "Enviar moedas"}
          </Button>
        </FormGroup>
      </Box>

      <Modal
        keepMounted
        open={openModal}
        onClose={handleCloseModal}
        aria-labelledby="keep-mounted-modal-title"
        aria-describedby="keep-mounted-modal-description"
      >
        <Box sx={style}>
          <Typography id="keep-mounted-modal-title" variant="h6" component="h2">
            Você está prestes a mandar moedas para:
          </Typography>
          <Typography
            id="keep-mounted-modal-description"
            variant="h4"
            component="h2"
            sx={{ my: 2, color: errorAluno ? 'red' : 'inherit', minHeight: '3rem' }} 
          >
            {isLoadingAluno && <CircularProgress size={30} />}
            {!isLoadingAluno && errorAluno && errorAluno}
            {!isLoadingAluno && !errorAluno && alunoParaConfirmacao?.nome}
            {!isLoadingAluno && !errorAluno && !alunoParaConfirmacao && "Nenhum aluno para exibir."}
          </Typography>
          <Typography id="keep-mounted-modal-title" variant="h6" component="h2">
            Você deseja enviar as moedas?
          </Typography>
          <Box
            sx={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              gap: "10px",
              marginTop: "20px",
            }}
          >
            <Button
              variant="contained"
              onClick={handleSubmit(processarEnvioMoedas)} 
              color="primary"
              disabled={isLoadingAluno || !!errorAluno || !alunoParaConfirmacao} 
            >
              Enviar
            </Button>
            <Button
              variant="outlined"
              onClick={handleCloseModal}
              color="primary"
            >
              Não enviar
            </Button>
          </Box>
        </Box>
      </Modal>
    </Box>
  );
}
